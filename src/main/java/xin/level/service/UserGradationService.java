package xin.level.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.level.domain.UserGradation;
import xin.level.mapper.UserGradationMapper;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Data
public class UserGradationService {

    @Autowired
    private UserGradationMapper userGradationMapper;

    // 用于存储用户信息的内存树结构
    private Map<Integer, UserGradation> userMap = new HashMap<>();

    // 在服务启动时，从数据库加载所有用户信息
    @PostConstruct
    public void init() {
        List<UserGradation> users = userGradationMapper.selectList(null);
        users.forEach(user -> userMap.put(user.getId(), user));
    }

    // 查询用户及其子节点
    public List<String> getUserAndDescendants(String userName) {
        return userMap.values().stream()
                .filter(user -> user.getUserName().equals(userName) || isDescendant(user, userName))
                .map(UserGradation::getUserName)
                .collect(Collectors.toList());
    }

    // 查询用户的父节点
    public String getParentUserName(String userName) {
        return userMap.values().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .map(user -> userMap.get(user.getParentId()).getUserName())
                .orElse(null);
    }

    // 添加子节点
    public void addChild(String parentUserName, String childUserName) {
        UserGradation parentUser = userMap.values().stream()
                .filter(user -> user.getUserName().equals(parentUserName))
                .findFirst()
                .orElse(null);

        if (parentUser != null) {
            UserGradation child = new UserGradation();
            child.setUserName(childUserName);
            child.setParentId(parentUser.getId());
            userGradationMapper.insert(child);
            userMap.put(child.getId(), child); // 更新内存中的树形结构
        }
    }

    // 辅助方法，检查是否为子节点
    private boolean isDescendant(UserGradation user, String parentUserName) {
        Integer parentId = user.getParentId();
        while (parentId != null) {
            UserGradation parent = userMap.get(parentId);
            if (parent != null && parent.getUserName().equals(parentUserName)) {
                return true;
            }
            parentId = parent.getParentId();
        }
        return false;
    }

    // 获取与指定用户同一层级的所有用户的userName
    public List<String> getUsersAtSameLevel(String userName) {
        UserGradation targetUser = findUserByName(userName);
        if (targetUser == null) {
            return new ArrayList<>(); // 如果用户不存在，返回空列表
        }

        int targetLevel = getLevel(targetUser);
        return userMap.values().stream()
                .filter(user -> getLevel(user) == targetLevel)
                .map(UserGradation::getUserName)
                .collect(Collectors.toList());
    }

    // 辅助方法：通过userName找到UserGradation对象
    private UserGradation findUserByName(String userName) {
        return userMap.values().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    // 辅助方法：获取用户在树中的层级
    private int getLevel(UserGradation user) {
        int level = 0;
        while (user.getParentId() != null) {
            user = userMap.get(user.getParentId());
            level++;
        }
        return level;
    }

    // 获取指定用户所在的层级，根节点在第零层
    public int getUserLevel(String userName) {
        UserGradation user = findUserByName(userName);
        if (user == null) {
            return -1; // 如果用户不存在，返回-1或者抛出异常
        }

        return getLevel(user); // 使用之前定义的getLevel方法
    }

    // 检查userName2是否是userName1的下层节点
    public boolean isDescendant(String userName1, String userName2) {
        UserGradation user1 = findUserByName(userName1);
        UserGradation user2 = findUserByName(userName2);

        if (user1 == null || user2 == null) {
            return false; // 如果任一用户不存在，返回false
        }

        return isDescendantHelper(user1.getId(), user2);
    }

    // 辅助递归方法，检查是否为子节点
    private boolean isDescendantHelper(Integer parentId, UserGradation child) {
        if (child.getParentId() == null) {
            return false;
        } else if (child.getParentId().equals(parentId)) {
            return true;
        } else {
            return isDescendantHelper(parentId, userMap.get(child.getParentId()));
        }
    }

    // 检查userName2是否是userName1的上层节点
    // 参数1是否是参数2的子节点
    public boolean isAncestor(String childrenUser, String parentUser) {
        UserGradation user1 = findUserByName(childrenUser);
        UserGradation user2 = findUserByName(parentUser);

        if (user1 == null || user2 == null) {
            return false; // 如果任一用户不存在，返回false
        }

        Integer parentId = user1.getParentId();
        while (parentId != null) {
            if (parentId.equals(user2.getId())) {
                return true;
            }
            UserGradation parent = userMap.get(parentId);
            parentId = parent != null ? parent.getParentId() : null;
        }
        return false;
    }

    // 获取指定userName的直接下级，不包括自己
    public List<String> getDirectDescendants(String userName) {
        UserGradation parentUser = findUserByName(userName);
        if (parentUser == null) {
            return new ArrayList<>(); // 如果用户不存在，返回空列表
        }

        Integer parentId = parentUser.getId();
        return userMap.values().stream()
                .filter(user -> parentId.equals(user.getParentId()))
                .map(UserGradation::getUserName)
                .collect(Collectors.toList());
    }

    // 查询指定userName的直属上级
    public String getDirectParent(String userName) {
        UserGradation user = findUserByName(userName);
        if (user == null || user.getParentId() == null) {
            return null; // 如果用户不存在或没有上级，返回null
        }

        UserGradation parentUser = userMap.get(user.getParentId());
        return parentUser != null ? parentUser.getUserName() : null;
    }
}

package xin.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xin.level.domain.UserGradation;
import xin.level.service.UserGradationService;

import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserGradationServiceTest {

    @Autowired
    private UserGradationService userGradationService;

    @Test
    public void testGetUserAndDescendants() {
        List<String> descendants = userGradationService.getUserAndDescendants("13223950610");
        descendants.forEach(System.out::println);

    }

    //测试层级：xin->xin2->18888888888->13513926685->666->777

    @Test
    public void testGetParentUserName() {
        String parentUserName = userGradationService.getParentUserName("18888888888");
        System.out.println(parentUserName);
    }

    @Test
    public void testAddChild() {
        userGradationService.addChild("13513926685", "00000000000");
        testPrintUserTree();
    }

    @Test
    public void testGetUsersAtSameLevel() {
        List<String> usersAtSameLevel = userGradationService.getUsersAtSameLevel("13721776906");
        usersAtSameLevel.forEach(System.out::println);
    }

    @Test
    public void testGetUserLevel() {
        System.out.println(userGradationService.getUserLevel("xin2"));
        System.out.println(userGradationService.getUserLevel("xin"));
    }

    @Test
    public void testPrintUserTree() {
        Map<Integer, UserGradation> userMap = userGradationService.getUserMap();

        // 找到根节点开始打印
        userMap.values().stream()
                .filter(user -> user.getParentId() == null)
                .forEach(rootUser -> printTree(rootUser, userMap, 0));
    }

    private void printTree(UserGradation user, Map<Integer, UserGradation> userMap, int level) {
        printIndentation(level);
        System.out.println(user.getUserName());

        userMap.values().stream()
                .filter(child -> child.getParentId() != null && child.getParentId().equals(user.getId()))
                .forEach(child -> printTree(child, userMap, level + 1));
    }

    private void printIndentation(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  "); // 两个空格的缩进
        }
    }

    @Test
    public void TestIsDescendant() {
        System.out.println(userGradationService.isDescendant("xin", "xin2"));
    }

    @Test
    public void TestIsAncestor() {
        testPrintUserTree();
        System.out.println(userGradationService.isAncestor("13721776906", "13223950610"));
    }

    @Test
    public void TestGetDirectDescendants() {
        testPrintUserTree();
        System.out.println(userGradationService.getDirectDescendants("xin"));
    }

    @Test
    public void TestGetDirectParent() {
        testPrintUserTree();
        System.out.println(userGradationService.getDirectParent("13223950610"));
    }
}

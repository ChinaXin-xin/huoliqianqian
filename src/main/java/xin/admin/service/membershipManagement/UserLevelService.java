package xin.admin.service.membershipManagement;

import xin.admin.domain.UserLevel;
import xin.admin.domain.membershipManagement.query.UserLevelRequestQuery;

public interface UserLevelService {
    void addUserLevel(UserLevel userLevel);
    void deleteUserLevelById(Long id);
    UserLevelRequestQuery getAllUserLevels(UserLevelRequestQuery userLevelRequestQuery);
    void updateUserLevel(UserLevel userLevel);
}

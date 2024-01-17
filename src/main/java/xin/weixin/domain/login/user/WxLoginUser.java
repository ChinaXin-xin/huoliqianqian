package xin.weixin.domain.login.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xin.common.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class WxLoginUser implements UserDetails {

    // 用户实体类，用于存储用户信息
    private User user;

    //存储权限信息
    private List<String> permissions;

    //作为中间变量，把存储权限信息的List<String>转为spring security能够识别的List<SimpleGrantedAuthority>
    @JSONField(serialize = false)
    List<SimpleGrantedAuthority> newList;

    public WxLoginUser(User user) {
        this.user = user;
    }

    public WxLoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    // 获取用户的权限集合。在这个例子中，返回null意味着没有指定权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (newList != null) {
            return newList;
        }

        if (permissions == null)
            return null;

        newList = permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return newList;
    }

    // 获取用户的密码，用于认证过程中的密码校验
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 获取用户的用户名，用于认证过程中的用户识别
    @Override
    public String getUsername() {
        return user.getUserName();
    }

    // 账户是否未过期。返回true表示账户未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未被锁定。返回true表示账户未被锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 凭证（密码）是否未过期。返回true表示密码未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否可用。返回true表示账户是可用的
    @Override
    public boolean isEnabled() {
        return true;
    }
}

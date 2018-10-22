package dolphin.account.domain.permission;

/**
 * Created by lyl on 2017/3/8.
 */
public class DataPermission {
    /**TODO
     * 数据权限
     * 例如界面上对地域维度进行授权。
     * dataPermission.dimension = area
     * dataPermission.value = "北京"
     * dataPermission.permission="READ"
     *
     * 将该信息，放到用户的 authorities 里。约定格式 例如 D:area:北京:READ
     *
     * 在调用数据的方法里
     *
     * @preFilter(hasPermisson(#area, "READ")
     * List getData( String area, ...)
     *
     *
     * 实现 MyPermissionEvaluator implements PermissionEvaluator
     *
     * hasPermission(Authentication authentication,
                Object targetDomainObject, Object permission) {
     *      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
     *   for (GrantedAuthority authority : authorities) {
     *      DataPermission dp = ...;
     *      if(dp.dimission == area && dp.value == targetDomainObject && dp.permission.contains(permission){
     *          return true;
     *      }
     *
     *      }
     *
     *   return false;
     *   }
     */
}

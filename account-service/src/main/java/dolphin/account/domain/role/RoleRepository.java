package dolphin.account.domain.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lyl on 16/5/25.
 */
@Repository
public interface RoleRepository  extends JpaRepository<Role, Integer> {
    public Role findRoleByItemName(String itemName);
}

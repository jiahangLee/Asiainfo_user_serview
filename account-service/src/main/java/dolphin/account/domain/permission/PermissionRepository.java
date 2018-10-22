package dolphin.account.domain.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lyl on 16/5/25.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    public Permission findByUniqueId(String uniqueId);
    List<Permission> findByParentId(int pid);
}

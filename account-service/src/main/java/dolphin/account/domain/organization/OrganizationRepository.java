package dolphin.account.domain.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lyl on 16/5/26.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    List<Organization> findByParentId(Integer parentId);

}

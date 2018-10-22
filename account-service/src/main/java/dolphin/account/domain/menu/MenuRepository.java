package dolphin.account.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lyl on 16/6/8.
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByParentId(Integer parentId);
}

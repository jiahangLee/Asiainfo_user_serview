package dolphin.account.domain.dict;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lyl on 16/6/3.
 */
@Repository
public interface DictRepository extends JpaRepository<Dict,Integer>{
}

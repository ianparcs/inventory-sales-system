package ph.parcs.rmhometiles.entity.log;

import org.springframework.stereotype.Repository;
import ph.parcs.rmhometiles.entity.inventory.item.EntityRepository;

import java.util.List;

@Repository
public interface LogRepository extends EntityRepository<Log, Integer> {

    @Override
    List<Log> findAll();
}

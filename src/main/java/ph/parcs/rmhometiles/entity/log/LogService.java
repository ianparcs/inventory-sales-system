package ph.parcs.rmhometiles.entity.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ph.parcs.rmhometiles.entity.inventory.item.BaseService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LogService extends BaseService<Log> {


    @Override
    public Log createDefault() {
        return null;
    }
}

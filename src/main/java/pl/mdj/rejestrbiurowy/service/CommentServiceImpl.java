package pl.mdj.rejestrbiurowy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mdj.rejestrbiurowy.service.interfaces.CommentService;
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
}

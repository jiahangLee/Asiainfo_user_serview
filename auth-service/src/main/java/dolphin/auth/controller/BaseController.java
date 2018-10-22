package dolphin.auth.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by lyl on 16/5/26.
 */
public interface BaseController<T, S> {

    public ResponseEntity<List<T>> list();
    public ResponseEntity<T> save(T t) throws Exception;
    public ResponseEntity<T> update(T t, S s) throws Exception;
    public ResponseEntity<T> delete(S s) throws Exception;
}

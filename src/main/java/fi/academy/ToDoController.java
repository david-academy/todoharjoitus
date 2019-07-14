package fi.academy;

import fi.academy.dao.ToDoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/todo")
@CrossOrigin(origins = "http://localhost:63342")
public
class ToDoController {
    private ToDoDao dao;

    @Autowired
    public ToDoController(@Qualifier("jdbc")ToDoDao dao){
        this.dao= dao;}

    @GetMapping("")
    public List<ToDo> listaaKaikki(){
        List<ToDo> kaikki = dao.haeKaikki();
        return kaikki;
    }

    @GetMapping("/{id}")
    public
    ResponseEntity<?> etsi (@PathVariable(name="id", required = true) int id){
        var haettu = dao.haeIdlla( id );
        if(!haettu.isPresent()){
            return ResponseEntity
                    .status( HttpStatus.NOT_FOUND )
                    .body( new Virheviesti(String.format("Id %d ei ole olemassa", id)) );
        }
        return ResponseEntity.ok( haettu.get() );
    }
    @PostMapping("")
    public ResponseEntity<?> luoUuusi(@RequestBody ToDo uus) {
        System.out.println("****** Luodaan uutta: " + uus);
        int id = dao.lisaa(uus);
        System.out.println("****** Luotu uusi: " + uus);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(uus);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> poista(@PathVariable int id) {
        ToDo poistettu = dao.poista(id);
        if (poistettu != null)
            return ResponseEntity.ok(poistettu);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Virheviesti(String.format("Id %d ei ole olemassa: ei poistettu", id)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editTodo(@RequestBody ToDo todo,
                                      @PathVariable(name = "id", required = true) int id) {
        int changedRows = dao.editTodo(id, todo);
        System.out.println(changedRows);
        if (changedRows !=0) {
            todo.setId(id);
            return ResponseEntity.ok(todo);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Virheviesti(String.format("Id %d ei ole olemassa: ei muutettu", id)));
    }

}

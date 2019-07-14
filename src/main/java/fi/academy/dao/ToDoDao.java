package fi.academy.dao;

import fi.academy.ToDo;

import java.util.List;
import java.util.Optional;

public
interface ToDoDao {
    List<ToDo> haeKaikki();
    Optional<ToDo> haeIdlla(int id);
    int lisaa(ToDo todo);
    ToDo poista(int id);
    int editTodo(int id, ToDo todo);
}

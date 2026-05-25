public interface CrudRepository<T> {
    void create(T object);
    T read(int id);
    void update(T object);
    void delete(int id);
}
import entities.Colonist;
import orm.Connector;
import orm.EntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException, NoSuchMethodException, InstantiationException, InvocationTargetException {

        Connector.createConnection("chono","chono0511","exams_mysql");
        EntityManager<Colonist> entityManager = new EntityManager<>(Connector.getConnection());
        Iterable<Colonist> iterable = entityManager.find(Colonist.class,"id > 60");
        for (Colonist colonist : iterable) {
            System.out.println(colonist.getId());
        }


    }

    public static void insertIntoTable(EntityManager<Colonist> entityManager) throws SQLException, IllegalAccessException {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1978);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.DAY_OF_MONTH, 20);
        Date birthDate = cal.getTime();

        Colonist colonist = new Colonist("Silvie","Vamos","1715745989",birthDate);
        entityManager.persist(colonist);
    }
}

import entities.Colonist;
import entities.User;
import orm.Connector;
import orm.EntityManager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException, NoSuchMethodException, InstantiationException, InvocationTargetException {

        Connector.createConnection("chono","chono0511","exams_mysql");
        EntityManager<User> entityManager = new EntityManager<>(Connector.getConnection());

        //Create Table
        //Uncomment the Code below
        /*
         entityManager.doCreate(User.class);
         */

      /*  Alter & Insert
        User loser = new User("Bravestone","jumanjii",43,new Date(System.currentTimeMillis()),"bravemann@get.bg",new BigDecimal("1500.56"));
        entityManager.persist(loser);
        User mouse = new User("Mouse","backpack",33,new Date(System.currentTimeMillis()),"minimouse@mint.com",new BigDecimal("2600.26"));
        entityManager.persist(mouse);
       */

        User user = entityManager.findFirst(User.class);
        entityManager.delete(user);
    }



    public static void iterateThroughSetOfRecords(EntityManager<Colonist> entityManager) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {

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

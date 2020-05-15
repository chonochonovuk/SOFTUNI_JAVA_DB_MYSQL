package engine;

import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Engine implements Runnable{

    private final EntityManager entityManager;

    public Engine(String database){
        this.entityManager = Persistence.createEntityManagerFactory(database).createEntityManager();

    }

    @Override
    public void run() {
//       Ex. 1 - Remove all towns with length > 5 symbols
//       this.removeFromDatabase();

//       Ex. 2 - checks if a given employee name is contained in the database or throw NoResultException
//       this.findWithWhereClause();

//       Ex. 3 - find Employees with salary > 50000
//        this.findWithWhereClauseTwo();

//        Ex. 4 - find from Department as foreign key
//        this.findWithWhereThree();

//        Ex. 5 - insert new in reference object and update existing
//          this.updateByGivenCriteria();

//        Ex. 6 - find by set of employees size
//          this.findByGrouping();

//        Ex. 7 - Get an employee by his/her id. Print only his/her first name, last name, job title and projects (only their names).
//          this.findWithRightJoin();

//        Ex. 8 - Write a program that prints the last 10 started projects.
//        this.orderByStartedDate();

//        Ex. 9 - Increase salary by departments
//          this.increaseSalaryByDepartments();

//        Ex. 10 - Retrieve Id By String and Delete
        this.deleteAllAddressesInTown();
    }

    private void deleteAllAddressesInTown() {
        String town = "Sofia";
        List<Address> addresses = this.entityManager.createQuery("SELECT a FROM Address AS a " +
                "WHERE a.town.name LIKE :name",Address.class)
                .setParameter("name",town).getResultList();
        int deleted = addresses.size();
        System.out.println("Addresses before update - " + deleted);
//        try {
//            Address address = this.entityManager.createQuery("SELECT a FROM Address AS a " +
//                    "WHERE a.id = 292 ",Address.class).getSingleResult();
//            Town town1 = this.entityManager.createQuery("SELECT t FROM Town AS t" +
//                    " WHERE t.name LIKE :name",Town.class)
//                    .setParameter("name",town)
//                    .getSingleResult();
//            this.entityManager.getTransaction().begin();
//            this.entityManager.detach(address);
//            address.setTown(town1);
//            this.entityManager.merge(address);
//            this.entityManager.getTransaction().commit();
//        }catch (NoResultException nre){
//            System.out.println("Something Went Wrong");
//        }
          this.entityManager.getTransaction().begin();
          addresses.forEach(this.entityManager::detach);
          addresses.forEach(a -> a.setTown(null));
          addresses.forEach(this.entityManager::merge);
          this.entityManager.flush();
          this.entityManager.getTransaction().commit();

    }

    private void increaseSalaryByDepartments() {
        List<Employee> employeesToUpdate = this.entityManager
                .createQuery("SELECT e FROM Employee AS e " +
                        "WHERE e.department.id IN (1,2,4,11) ",Employee.class)
                .getResultList();
        this.entityManager.getTransaction().begin();
        employeesToUpdate.forEach(this.entityManager::detach);
        employeesToUpdate.forEach(e -> e.setSalary(e.getSalary().multiply(new BigDecimal("1.12"))));
        employeesToUpdate.forEach(this.entityManager::merge);
        this.entityManager.flush();
        this.entityManager.getTransaction().commit();
    }

    private void removeFromDatabase(){
        List<Town> townsToRemove = this.entityManager.createQuery("SELECT t FROM Town AS t" +
                " WHERE length(t.name) > 5 ",Town.class)
                .getResultList();
        this.entityManager.getTransaction().begin();
        townsToRemove.forEach(this.entityManager::detach);
        townsToRemove.forEach(t -> t.setName(t.getName().toUpperCase()));
        townsToRemove.forEach(this.entityManager::merge);
        this.entityManager.flush();
        this.entityManager.getTransaction().commit();
        System.out.println();
    }

    private void findWithWhereClause(){
        String match = "Svet Nakov";
        try {
            Employee findEmployee = this.entityManager.createQuery("SELECT e FROM Employee AS e " +
                    "WHERE concat(e.firstName,' ',e.lastName) = :name ",Employee.class).setParameter("name",match)
                    .getSingleResult();
            String regex = (findEmployee != null) ? "Yes" : "Nooo";

            System.out.println(regex);
        }catch (NoResultException nre){
            System.out.println("No");
        }

    }

    private void findWithWhereClauseTwo(){
           this.entityManager.createQuery("SELECT e FROM Employee AS e" +
                " WHERE e.salary > 50000",Employee.class)
                   .getResultStream().forEach(employee -> System.out.println(employee.getFirstName()));
    }
    private void findWithWhereThree(){
        this.entityManager.createQuery("SELECT e FROM Employee AS e" +
                " WHERE e.department.name LIKE 'Research and Development'" +
                " ORDER BY e.salary ,e.id",Employee.class).getResultStream()
                .forEach(employee -> System.out.printf("%s %s from Research and Development - $%.2f%n",
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getSalary()));
    }

    private void updateByGivenCriteria(){
        String employeeName = "Hartwig";
        try {
            Employee employee = this.entityManager.createQuery("SELECT e FROM Employee AS e " +
                    "WHERE e.lastName = :lastName", Employee.class).setParameter("lastName", employeeName)
                    .getSingleResult();

            Address address = this.createNewAddress("Vitoshka 15");
            employee.setAddress(address);
            this.entityManager.getTransaction().begin();
            this.entityManager.detach(employee);
            this.entityManager.merge(employee);
            this.entityManager.flush();
            this.entityManager.getTransaction().commit();
        }catch (NoResultException nre){
            System.out.println("Not found in Employees!");
        }

    }

    private Address createNewAddress(String text) {
        Address address = new Address();
        address.setText(text);

        this.entityManager.getTransaction().begin();
        this.entityManager.persist(address);
        this.entityManager.getTransaction().commit();
        return address;
    }

    private void findByGrouping(){
        this.entityManager.createQuery("SELECT a FROM Address AS a" +
                " ORDER BY a.employees.size DESC ",Address.class)
                .setMaxResults(10)
                .getResultStream()
                .forEach(a -> System.out.printf("%s, %s - %d employees%n",a.getText(),a.getTown(),a.getEmployees().size()));
    }

    private void findWithRightJoin(){
        int employeeId = 83;
        try {
           Employee e = this.entityManager.createQuery("SELECT e FROM Employee AS e " +
                    "WHERE e.id = :id ", Employee.class).setParameter("id", employeeId)
                    .getSingleResult();
            System.out.printf("%s %s - %s%n",e.getFirstName(),e.getLastName(),e.getJobTitle());
            if (e.getProjects().size() > 0){
                e.getProjects().stream().sorted(Comparator.comparing(Project::getName))
                        .forEach(p -> System.out.println(p.getName()));
            }else {
                System.out.println("None");
            }
        }catch (NoResultException nre){
            System.out.println("Id not exist!!!");
        }
    }

    private void orderByStartedDate(){
        this.entityManager.createQuery("SELECT p FROM Project AS p " +
                "ORDER BY p.startDate DESC ",Project.class)
                .setMaxResults(10)
                .getResultStream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(x -> System.out.printf(" Project name:%s%n Project Description:%s%n " +
                        "Project Start Date:%s%n Project End Date:%s%n",x.getName()
                        ,x.getDescription()
                        ,x.getStartDate()
                        ,x.getEndDate()));
    }
}

package engine;

import entity.gringots.WizardDeposit;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Engine implements Runnable {

    private EntityManager entityManager;

    public Engine(String persistenceUnit) {
        this.entityManager = Persistence
                .createEntityManagerFactory(persistenceUnit)
                .createEntityManager();
    }

    @Override
    public void run() {

        WizardDeposit wizardDeposit = new WizardDeposit();
        wizardDeposit.setLastName("Chonov");
        wizardDeposit.setAge(25);

        this.entityManager.getTransaction().begin();
        this.entityManager.persist(wizardDeposit);
        this.entityManager.getTransaction().commit();

    }
}

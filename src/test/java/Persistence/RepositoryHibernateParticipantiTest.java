package Persistence;

import Domain.Participant;
import org.junit.Test;

import java.util.ArrayList;

public class RepositoryHibernateParticipantiTest {

    @Test
    public void findParticipantiByProba() {
    }

    @Test
    public void findParticipantiByVarsta() {
    }

    @Test
    public void findParticipantiByProba_Varsta() {
    }

    @Test
    public void findLastAdded() {
    }

    @Test
    public void findOne() {
    }

    @Test
    public void findAll() throws RepositoryException {
        RepositoryHibernateParticipanti repo = new RepositoryHibernateParticipanti();
        Participant part1 = new Participant(1,"Ioan",10,1);
        Participant part2 = new Participant(1,"Maria",14,2);
        Participant part3 = new Participant(1,"Elena",8,0);
        ArrayList<Participant> participanti = new ArrayList<>();
        participanti.add(part1);
        participanti.add(part2);
        participanti.add(part3);
        repo.save(part1);
        repo.save(part2);
        repo.save(part3);
        ArrayList<Participant> items = repo.findAll();
        for (int i=0;i<3;i++){
            System.out.println(participanti.get(i)+"------"+items.get(i));
            assert (participanti.get(i) == items.get(i));
        }
    }
}
package Persistence;

import Domain.Organizator;

public interface IOrganizatoriRepository extends IRepository<Integer, Organizator> {

    public Organizator findOnebyUserName(String userName);

}

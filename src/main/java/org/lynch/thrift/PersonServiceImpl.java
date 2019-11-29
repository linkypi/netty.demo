package org.lynch.thrift;

import org.apache.thrift.TException;

public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public Person getByUserName(String username) throws DataException, TException {

        System.out.println("get client param: "+ username);
        Person person = new Person();
        person.setAge(20);
        person.setUsername(username);
        person.setMarried(false);
        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
         System.out.println("get person:"+ person.toString());
    }
}

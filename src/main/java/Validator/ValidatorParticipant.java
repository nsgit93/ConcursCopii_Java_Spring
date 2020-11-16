package Validator;

import Domain.Participant;

public class ValidatorParticipant implements IValidator<Participant> {
    @Override
    public void validate(Participant entity) throws ValidationException {
        StringBuffer msg=new StringBuffer();
        if (entity.getId()<0)
            msg.append("Id-ul nu poate fi negativ!");
        if (entity.getVarsta()< 6 || entity.getVarsta() > 15)
            msg.append("Varsta trebuie sa fie cuprinsa intre 6 si 15 ani!");
        if (entity.getNume().equals(""))
            msg.append("Trebuie introdus un nume!");
        if (msg.length()>0)
            throw new ValidationException(msg.toString());
    }
}

package listener;

import entity.PersonalAccountEntity;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import utlis.EmailSender;
import validator.Error;

import java.util.Properties;


public class SendAuthEmailListener implements PostInsertEventListener {
    private static final Class<PersonalAccountEntity> personalAccountEntityClass = PersonalAccountEntity.class;
    private static final Properties properties = EmailSender.defaultTLSProperties();
    private final static Error cantSendConfirmationEmail = new Error("Cant send confirmation email", "Wrong email. " +
            "Please, check your email again");

    @Override
    public void onPostInsert(PostInsertEvent event) {

//        if (event.getEntity().getClass() == personalAccountEntityClass) {
//            PersonalAccountEntity entity = (PersonalAccountEntity) event.getEntity();
//            try {
//                // todo Only my email
//                EmailSender.sendTLSEmail(properties, "rosomaha.2009@mail.ru", "link");
//            } catch (EmailSendException e) {
//                ValidationErrors.getInstance().addCreateAccountError(cantSendConfirmationEmail);
//            }
//        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
}

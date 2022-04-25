package at.demo.utils;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Properties;
import java.util.stream.Stream;

public class CustomIdentifierGenerator implements IdentifierGenerator, Configurable {
    private String prefix;

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object obj)
            throws HibernateException {

        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        Stream ids = session.createQuery(query).stream();

        // Current year
        int yearLastDigits = Calendar.getInstance().get(Calendar.YEAR) % 100;

        Long max = ids.map(o -> o.toString().replaceAll(prefix + yearLastDigits + "-", ""))
                .mapToLong(o -> Long.parseLong(o.toString()))
                .max()
                .orElse(0L);



        String leftPadded = String.format("%07d",  (max + 1));

        return prefix + yearLastDigits + "-" + leftPadded;
    }

    @Override
    public void configure(Type type, Properties properties,
                          ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
    }
}
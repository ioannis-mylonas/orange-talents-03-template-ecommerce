package bootcamp.mercado.validator;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {
	private EntityManager manager;
	private Class<?> target;
	private String field;
	
	public UniqueValidator(EntityManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void initialize(Unique constraint) {
		target = constraint.target();
		field = constraint.field();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return validate(value, target, field);
	}
	
	public boolean validate(Object value, Class<?> target, String field) {
		String q = String.format("SELECT 1 FROM %s s WHERE lower(trim(s.%s))=lower(trim(:value))",
				target.getName(), field);
		
		Query query = manager.createQuery(q);
		query.setParameter("value", value);
		List<?> res = query.getResultList();
		
		assert(res.size() <= 1);
		
		return res.isEmpty();
	}
}

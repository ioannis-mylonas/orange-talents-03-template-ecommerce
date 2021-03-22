package bootcamp.mercado.validator;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsValidator implements ConstraintValidator<Exists, Object> {
	private EntityManager manager;
	private Class<?> target;
	private String field;
	
	public ExistsValidator(EntityManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void initialize(Exists constraint) {
		this.target = constraint.target();
		this.field = constraint.field();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) return true;
		
		String q = String.format("SELECT s from %s s WHERE s.%s=:value",
				target.getName(), field);
		
		Query query = manager.createQuery(q);
		query.setParameter("value", value);
		
		return !query.getResultList().isEmpty();
	}
}

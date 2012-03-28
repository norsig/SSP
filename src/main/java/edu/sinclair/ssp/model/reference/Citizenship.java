package edu.sinclair.ssp.model.reference;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Citizenship extends AbstractReference implements Serializable {

	private static final long serialVersionUID = -8799814200256630616L;

	public Citizenship() {
		super();
	}

	public Citizenship(UUID id) {
		super(id);
	}

	public Citizenship(UUID id, String name) {
		super(id, name);
	}

	public Citizenship(UUID id, String name, String description) {
		super(id, name, description);
	}

	/**
	 * Overwrites simple properties with the parameter's properties.
	 * 
	 * @param source
	 *            Source to use for overwrites.
	 */
	public void overwrite(Citizenship source) {
		this.setName(source.getName());
		this.setDescription(source.getDescription());
	}
}

package br.com.fullcycle.infrastructure.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;
import java.util.function.Function;

import br.com.fullcycle.domain.DomainEvent;

@Entity(name = "Outbox")
@Table(name = "outbox")
public class OutboxEntity {

    @Id
    private UUID id;
    
    @Column(columnDefinition = "JSON", length = 4_000)
    private String content;
    
    private boolean published;

    public OutboxEntity() {
    }

    public OutboxEntity(UUID id, String content, boolean published) {
        this.id = id;
        this.content = content;
        this.published = published;
    }

    public static OutboxEntity mapFrom(final DomainEvent domainEvent, final Function<DomainEvent, String> toJson) {
    	return new OutboxEntity(
			UUID.fromString(domainEvent.domainEventId()),
			toJson.apply(domainEvent),
			false
		);
    }

	public UUID getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public boolean isPublished() {
		return published;
	}
	
	public OutboxEntity notePublished() {
		this.published = true;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OutboxEntity other = (OutboxEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

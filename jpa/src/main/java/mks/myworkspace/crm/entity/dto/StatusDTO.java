package mks.myworkspace.crm.entity.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
    private String siteId;
    private String name;
    private String backgroundColor;
    private Long seqno;
}

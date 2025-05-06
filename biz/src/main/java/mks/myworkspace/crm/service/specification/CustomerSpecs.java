package mks.myworkspace.crm.service.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import mks.myworkspace.crm.entity.Customer;

public class CustomerSpecs {
	
	public static Specification<Customer> matchMainStatusName(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("mainStatus").get("name"),"%" +keyword +"%");
    }
	public static Specification<Customer> matchSubStatusName(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("subStatus").get("name"),"%" +keyword +"%");
    }
	public static Specification<Customer> matchProfession(Long professionId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("profession"), professionId);
    }
    public static Specification<Customer> matchListProfession(List<Long> profession) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.in(root.get("profession").get("id")).value(profession);
        };
    }
    public static Specification<Customer> matchStatus(Long statusId) {
    	return (root, query, criteriaBuilder) ->criteriaBuilder.or(criteriaBuilder.equal(root.get("mainStatus").get("id"), statusId),criteriaBuilder.equal(root.get("subStatus").get("id"), statusId)) ;
    }
    
    public static Specification<Customer> matchPhone(String phone) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("phone"),"%" + phone + "%");
    }
    public static Specification<Customer> matchContactPerson(String contactPerson) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("contactPerson"),"%" + contactPerson+ "%");
    }
    
//    public static Specification<Customer> matchAddress(String address) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("address"),"%" + address+ "%");
//    }
    
	public static Specification<Customer> matchAddress(String keyword) {
    	return (root, query, criteriaBuilder)  -> {
    		if(keyword == null || keyword.trim().isEmpty()) {
    			return criteriaBuilder.conjunction(); //Khong filter neu keyword trong
    		}
    		 var addressPath = root.get("address");
    		 String likePattern = "%" + keyword.trim() + "%";
    		 
    		 return criteriaBuilder.or(
    				 criteriaBuilder.like(addressPath.get("street"), likePattern),
    				 criteriaBuilder.like(addressPath.get("ward"), likePattern),
    		         criteriaBuilder.like(addressPath.get("district"), likePattern),
    		         criteriaBuilder.like(addressPath.get("state"), likePattern),
    		         criteriaBuilder.like(addressPath.get("country"), likePattern)
    		 );
    	};
    }
        
    public static Specification<Customer> matchEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("email"),"%" + email+ "%");
        
    }
    public static Specification<Customer> matchNameCompany(String nameCompany) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("companyName"),"%" + nameCompany + "%");
    }
    public static Specification<Customer> matchaccountStatusTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("accountStatus"), true);
    }
    public static Specification<Customer> matchReponsiblePersonName(String keyword)
    {
    	return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("responsiblePerson").get("name"),"%" + keyword + "%");
    }
    public static Specification<Customer> matchProfession(String keyword)
    {
    	return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("profession").get("name"),"%" + keyword + "%");
    }
    public static Specification<Customer> matchNote(String keyword)
    {
    	return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("note"),"%" + keyword + "%");
    }
}

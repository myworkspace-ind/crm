package mks.myworkspace.crm.repository;

import java.util.HashMap; // �? s? d?ng HashMap
import java.util.Map;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Customer;

@Repository
public class AppRepository {
	@Autowired
	@Qualifier("jdbcTemplate0")
	private JdbcTemplate jdbcTemplate0;

	/**
	 * @param entities
	 * @return
	 */
	public Long saveOrUpdate(Customer customer) {
		Long id;
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_customer")
				.usingGeneratedKeyColumns("id");

		if (customer.getId() == null) {
			// Save new customer
			id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(customer)).longValue();
		} else {
			// Update existing customer
			update(customer);
			id = customer.getId();
		}

		return id;

	}

	public List<Long> saveOrUpdate(List<Customer> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_customer")
				.usingGeneratedKeyColumns("id");

		Long id;
		for (Customer e : entities) {
			if (e.getId() == null) {
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
			} else {
				// Update
				update(e);
				id = e.getId();
			}

			ids.add(id);
		}

		return ids;
	}

	private void update(Customer e) {
		// TODO Auto-generated method stub

	}

	public void deleteCustomerStatusByCustomerIds(List<Long> customerIds) {
		if (customerIds == null || customerIds.isEmpty()) {
			return; // Kh�ng l�m g� n?u danh s�ch r?ng
		}

		String sql = "DELETE FROM customer_status WHERE customer_id IN (" + customerIds.stream().map(id -> "?") // T?o
																												// c�c
																												// tham
																												// s?
				.collect(Collectors.joining(",")) + ")";

		// Th?c hi?n c�u l?nh x�a
		jdbcTemplate0.update(sql, customerIds.toArray()); // Truy?n m?ng ID
	}

	// g?i phuong th?c n�y tru?c khi x�a kh�ch h�ng
	public void deleteCustomersByIds(List<Long> customerIds) {
		deleteCustomerStatusByCustomerIds(customerIds); // X�a tru?c c�c b?n ghi li�n quan
		// Ti?n h�nh x�a kh�ch h�ng
		String sql = "DELETE FROM crm_customer WHERE id IN ("
				+ customerIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

		jdbcTemplate0.update(sql, customerIds.toArray());
	}

}

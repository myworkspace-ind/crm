package mks.myworkspace.crm.repository;

import java.util.List;
import java.util.HashMap; // Để sử dụng HashMap
import java.util.Map;
import java.util.stream.Collectors;

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

	private void update(Customer e) {
		// TODO Auto-generated method stub

	}

	public void deleteCustomerStatusByCustomerIds(List<Long> customerIds) {
		if (customerIds == null || customerIds.isEmpty()) {
			return; // Không làm gì nếu danh sách rỗng
		}

		String sql = "DELETE FROM customer_status WHERE customer_id IN (" + customerIds.stream().map(id -> "?") // Tạo
																												// các
																												// tham
																												// số ?
				.collect(Collectors.joining(",")) + ")";

		// Thực hiện câu lệnh xóa
		jdbcTemplate0.update(sql, customerIds.toArray()); // Truyền mảng ID
	}

	// Sau đó gọi phương thức này trước khi xóa khách hàng
	public void deleteCustomersByIds(List<Long> customerIds) {
		deleteCustomerStatusByCustomerIds(customerIds); // Xóa trước các bản ghi liên quan
		// Tiến hành xóa khách hàng
		String sql = "DELETE FROM crm_customer WHERE id IN ("
				+ customerIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

		jdbcTemplate0.update(sql, customerIds.toArray());
	}

}

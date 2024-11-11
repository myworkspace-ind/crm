package mks.myworkspace.crm.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.Order;

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
			return;
		}

		// Tạo các tham số
		String sql = "DELETE FROM customer_status WHERE customer_id IN ("
				+ customerIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

		// Thực hiện câu lệnh xóa
		jdbcTemplate0.update(sql, customerIds.toArray()); // Truyền mảng ID
	}

	public void deleteCustomersByIds(List<Long> customerIds) {
		deleteCustomerStatusByCustomerIds(customerIds); // Xóa trước các bản ghi liên quan
		// Tiến hành xóa khách hàng
		String sql = "DELETE FROM crm_customer WHERE id IN ("
				+ customerIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

		jdbcTemplate0.update(sql, customerIds.toArray());
	}

	public Long saveOrUpdateOrder(Order order) {
		Long id;
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_order")
				.usingGeneratedKeyColumns("id");

		if (order.getId() == null) {
			id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(order)).longValue();
		} else {
			updateOrder(order);
			id = order.getId();
		}
		return id;
	}

	private void updateOrder(Order order) {
		String updateSql = "UPDATE crm_order SET site_id = ?, name = ?, code = ?, create_date = ?, delivery_date = ?, "
				+ "transportation_method = ?, customer_requirement = ?, order_cate_id = ?, cus_id = ?, "
				+ "order_status_id = ?, goods_category_id = ? WHERE id = ?";

		jdbcTemplate0.update(updateSql, order.getSiteId(), order.getName(), order.getCode(), order.getCreateDate(),
				order.getDeliveryDate(), order.getTransportationMethod(), order.getCustomerRequirement(),
				order.getOrderCategory() != null ? order.getOrderCategory().getId() : null,
				order.getCustomer() != null ? order.getCustomer().getId() : null,
				order.getOrderStatus() != null ? order.getOrderStatus().getId() : null,
				order.getGoodsCategory() != null ? order.getGoodsCategory().getId() : null, order.getId());
	}
}

package mks.myworkspace.crm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderStatus;

@Repository
@Slf4j
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
		String sql = "DELETE FROM customer_status WHERE customer_id IN ("
				+ customerIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

		jdbcTemplate0.update(sql, customerIds.toArray());
	}

	public void deleteCustomersByIds(List<Long> customerIds) {
		// deleteCustomerStatusByCustomerIds(customerIds);
		String sql = "DELETE FROM crm_customer WHERE id IN ("
				+ customerIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

		jdbcTemplate0.update(sql, customerIds.toArray());
	}
	
//	public Order saveOrUpdateOrder(Order order) {
//	    if (order.getId() == null) {
//	        log.debug("Inserting new order"); // Log when inserting a new order
//	        Long newId = createOrder(order); // Tạo mới order và lấy ID mới
//	        order.setId(newId); // Cập nhật ID mới vào đối tượng order
//	    } else {
//	        log.debug("Updating existing order with ID: {}", order.getId()); // Log when updating
//	        updateOrder(order); // Cập nhật thông tin order
//	    }
//
//	    log.debug("Final Order ID after saveOrUpdate: {}", order.getId());
//	    return order; // Trả về đối tượng order đã được cập nhật
//	}
	

	public Long saveOrUpdateOrder(Order order) {

		Long id = null;

		if (order.getId() == null) {
			log.debug("Inserting new order"); // Log when inserting a new order
			createOrder(order);
			id = order.getId();

		} else {
			log.debug("Updating existing order with ID: {}", order.getId()); // Log when updating
			updateOrder(order);
			id = order.getId();
		}

		log.debug("Resulting ID after saveOrUpdate: {}", id);
		return id;
	}

	public Long createOrder(Order order) {
		Long id;
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_order")
				.usingGeneratedKeyColumns("id");

		id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(order)).longValue();
		log.debug("New ID: {}", id);
		return id;

	}

	@SuppressWarnings("deprecation")
	private void updateOrder(Order order) {
		// Truy vấn dữ liệu đơn hàng cũ dựa trên ID
		String selectSql = "SELECT * FROM crm_order WHERE id = ?";

		// Truy vấn để lấy dữ liệu đơn hàng cũ
		Order existingOrder = jdbcTemplate0.queryForObject(selectSql, new Object[] { order.getId() },
				new RowMapper<Order>() {
					public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
						Order ord = new Order();
						ord.setId(rs.getLong("id"));
						ord.setSiteId(rs.getString("site_id"));
						ord.setName(rs.getString("name"));
						ord.setCode(rs.getString("code"));
						ord.setCreateDate(rs.getDate("create_date"));
						ord.setDeliveryDate(rs.getDate("delivery_date"));
						ord.setTransportationMethod(rs.getString("transportation_method"));
						ord.setCustomerRequirement(rs.getString("customer_requirement"));

						// Sender
						Customer sender = new Customer();
						long senderId = rs.getLong("sender_id");
						sender.setId(senderId);
						ord.setSender(sender);

						// Receiver
						Customer receiver = new Customer();
						long receiverId = rs.getLong("receiver_id");
						receiver.setId(receiverId);
						ord.setReceiver(receiver);

						GoodsCategory goodsCategory = new GoodsCategory();
						goodsCategory.setId(rs.getLong("goods_category_id"));
						ord.setGoodsCategory(goodsCategory);

						OrderStatus orderStatus = new OrderStatus();
						orderStatus.setId(rs.getLong("order_status_id"));
						ord.setOrderStatus(orderStatus);

						return ord;
					}
				});

		// SQL để cập nhật dữ liệu đơn hàng
		String updateSql = "UPDATE crm_order SET name = ?, code = ?, create_date = ?, "
				+ "delivery_date = ?, transportation_method = ?, customer_requirement = ?, "
				+ "sender_id = ?, receiver_id = ?, order_status_id = ?, goods_category_id = ? " + "WHERE id = ?";

		// Thực thi câu lệnh cập nhật
		jdbcTemplate0.update(updateSql, order.getName() != null ? order.getName() : existingOrder.getName(),
				order.getCode() != null ? order.getCode() : existingOrder.getCode(),
				order.getCreateDate() != null ? order.getCreateDate() : existingOrder.getCreateDate(),
				order.getDeliveryDate() != null ? order.getDeliveryDate() : existingOrder.getDeliveryDate(),
				order.getTransportationMethod() != null ? order.getTransportationMethod()
						: existingOrder.getTransportationMethod(),
				order.getCustomerRequirement() != null ? order.getCustomerRequirement()
						: existingOrder.getCustomerRequirement(),

				order.getSender() != null ? order.getSender().getId() : existingOrder.getSender().getId(),
				order.getReceiver() != null ? order.getReceiver().getId() : existingOrder.getReceiver().getId(),

				order.getOrderStatus() != null ? order.getOrderStatus().getId()
						: existingOrder.getOrderStatus().getId(),
				order.getGoodsCategory() != null ? order.getGoodsCategory().getId()
						: existingOrder.getGoodsCategory().getId(),
				order.getId());
	}

}

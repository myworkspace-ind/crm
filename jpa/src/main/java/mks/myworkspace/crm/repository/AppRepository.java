package mks.myworkspace.crm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;

@Repository
@Slf4j
public class AppRepository {
	@Autowired
	@Qualifier("jdbcTemplate0")
	private JdbcTemplate jdbcTemplate0;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	OrderStatusRepository orderStatusRepository;

	@Autowired
	GoodsCategoryRepository goodsCategoryRepository;

	@Autowired
	OrderCategoryRepository orderCategoryRepository;

	/**
	 * @param entities
	 * @return
	 */
//	public Long saveOrUpdate(Customer customer) {
//		Long id;
//		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_customer")
//				.usingGeneratedKeyColumns("id");
//
//		if (customer.getId() == null) {
//			// Save new customer
//			id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(customer)).longValue();
//		} else {
//			// Update existing customer
//			update(customer);
//			id = customer.getId();
//		}
//
//		return id;
//
//	}
	public List<Long> saveOrUpdateOrderCategory(List<OrderCategory> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.
		
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_ordercategory").usingGeneratedKeyColumns("id");
		
		Long id;
		for (OrderCategory e : entities) {
			if (e.getId() == null) {
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
			} else {
				//Update
				//update(e);
				id = e.getId();
			}

			ids.add(id);
		}
		
		return ids;
	}
	
	public Long saveOrUpdate(Customer customer) {
		Long id;
		
		if (customer.getId() == null) {
			log.debug("Inserting new customer");
			id = createCustomer(customer);
		} else {
			log.debug("Updating existing customer with ID: {}", customer.getId());
			updateCustomer(customer);
			id = customer.getId();
		}

		log.debug("Resulting ID after saveOrUpdate: {}", id);
		return id;

	}

	private int updateCustomer(Customer customer) {
	    // Tạo bản đồ chứa các giá trị cần cập nhật
	    Map<String, Object> parameters = new HashMap<>();
	    
	    // Thêm các trường cố định trong entity (không có liên kết bảng)
	    parameters.put("address", customer.getAddress());
	    parameters.put("company_name", customer.getCompanyName());
	    parameters.put("contact_person", customer.getContactPerson());
	    //parameters.put("updated_at", customer.getUpdatedAt()); // Giả sử bạn có trường `updated_at`
	    parameters.put("email", customer.getEmail());
	    parameters.put("note", customer.getNote());
	    parameters.put("phone", customer.getPhone());
	    
	    // Thêm các khóa ngoại
	    parameters.put("main_status_id", customer.getMainStatus() != null ? customer.getMainStatus().getId() : null);
	    parameters.put("sub_status_id", customer.getSubStatus() != null ? customer.getSubStatus().getId() : null);
	    parameters.put("profession_id", customer.getProfession() != null ? customer.getProfession().getId() : null);
	    parameters.put("responsible_person_id", customer.getResponsiblePerson() != null ? customer.getResponsiblePerson().getId() : null);

	    // Chỉ định điều kiện cập nhật (thường là theo ID)
	    String sql = "UPDATE crm_customer SET " +
	                 "address = :address, " +
	                 "company_name = :company_name, " +
	                 "contact_person = :contact_person, " +
	                 //"updated_at = :updated_at, " +
	                 "email = :email, " +
	                 "note = :note, " +
	                 "phone = :phone, " +
	                 "main_status_id = :main_status_id, " +
	                 "sub_status_id = :sub_status_id, " +
	                 "profession_id = :profession_id, " +
	                 "responsible_person_id = :responsible_person_id " +
	                 "WHERE id = :id";

	    // Thêm ID vào parameters
	    parameters.put("id", customer.getId());

	    // Thực thi câu lệnh SQL với NamedParameterJdbcTemplate
	    int rowsAffected = new NamedParameterJdbcTemplate(jdbcTemplate0).update(sql, parameters);
	    log.debug("Updated rows: {}", rowsAffected);
	    return rowsAffected;
	}


	private Long createCustomer(Customer customer) {
		Long id;
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_customer")
				.usingGeneratedKeyColumns("id");

		Map<String, Object> parameters = new HashMap<>();
		// Thêm các trường cố định trong entity (không có liên kết bảng)
		parameters.put("address", customer.getAddress());
		parameters.put("company_name", customer.getCompanyName());
		parameters.put("contact_person", customer.getContactPerson());
		parameters.put("created_at", customer.getCreatedAt());
		parameters.put("email", customer.getEmail());
		parameters.put("note", customer.getNote());
		parameters.put("phone", customer.getPhone());
		
		// Thêm các khóa ngoại
		parameters.put("main_status_id", customer.getMainStatus() != null ? customer.getMainStatus().getId() : null);
		parameters.put("sub_status_id", customer.getSubStatus() != null ? customer.getSubStatus().getId() : null);
		parameters.put("profession_id", customer.getProfession() != null ? customer.getProfession().getId() : null);
		parameters.put("responsible_person_id", customer.getResponsiblePerson() != null ? customer.getResponsiblePerson().getId() : null);
		
		id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
		log.debug("New ID: {}", id);
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

	public void deleteOrderById(Long orderId) {
		if (orderId == null) {
			return;
		}

		String sql = "DELETE FROM crm_order WHERE id = ?";
		jdbcTemplate0.update(sql, orderId);
	}

	public Long saveOrUpdateOrder(Order order) {
		Long id;

		// Fetch related entities
		if (order.getSender() != null && order.getSender().getId() != null) {
			order.setSender(customerRepository.findById(order.getSender().getId())
					.orElseThrow(() -> new IllegalArgumentException("Sender not found")));
		}
		if (order.getReceiver() != null && order.getReceiver().getId() != null) {
			order.setReceiver(customerRepository.findById(order.getReceiver().getId())
					.orElseThrow(() -> new IllegalArgumentException("Receiver not found")));
		}
		if (order.getGoodsCategory() != null && order.getGoodsCategory().getId() != null) {
			order.setGoodsCategory(goodsCategoryRepository.findById(order.getGoodsCategory().getId())
					.orElseThrow(() -> new IllegalArgumentException("GoodsCategory not found")));
		}
		if (order.getOrderStatus() != null && order.getOrderStatus().getId() != null) {
			order.setOrderStatus(orderStatusRepository.findById(order.getOrderStatus().getId())
					.orElseThrow(() -> new IllegalArgumentException("OrderStatus not found")));
		}
		if (order.getOrderCategory() != null && order.getOrderCategory().getId() != null) {
			order.setOrderCategory(orderCategoryRepository.findById(order.getOrderCategory().getId())
					.orElseThrow(() -> new IllegalArgumentException("OrderCategory not found")));
		}

		// Insert or update logic
		if (order.getId() == null) {
			log.debug("Inserting new order");
			id = createOrder(order);
		} else {
			log.debug("Updating existing order with ID: {}", order.getId());
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

		Map<String, Object> parameters = new HashMap<>();
		// Thêm các trường cố định trong entity (không có liên kết bảng)
		parameters.put("code", order.getCode());
		parameters.put("create_date", order.getCreateDate());
		parameters.put("delivery_date", order.getDeliveryDate());
		parameters.put("customer_requirement", order.getCustomerRequirement());
		parameters.put("transportation_method", order.getTransportationMethod());
		parameters.put("address", order.getAddress());

		// Thêm các khóa ngoại
		parameters.put("sender_id", order.getSender() != null ? order.getSender().getId() : null);
		parameters.put("receiver_id", order.getReceiver() != null ? order.getReceiver().getId() : null);
		parameters.put("goods_category_id", order.getGoodsCategory() != null ? order.getGoodsCategory().getId() : null);
		parameters.put("order_status_id", order.getOrderStatus() != null ? order.getOrderStatus().getId() : null);
		parameters.put("order_cate_id", order.getOrderCategory() != null ? order.getOrderCategory().getId() : null);

		id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
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
						ord.setAddress(rs.getString("address"));

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
				+ "delivery_date = ?, transportation_method = ?, customer_requirement = ?, address = ?, "
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
				order.getAddress() != null ? order.getAddress() : existingOrder.getAddress(),
				order.getSender() != null ? order.getSender().getId() : existingOrder.getSender().getId(),
				order.getReceiver() != null ? order.getReceiver().getId() : existingOrder.getReceiver().getId(),
				order.getOrderStatus() != null ? order.getOrderStatus().getId()
						: existingOrder.getOrderStatus().getId(),
				order.getGoodsCategory() != null ? order.getGoodsCategory().getId()
						: existingOrder.getGoodsCategory().getId(),
				order.getId());
	}

	public Long updateOrderStatus(Order order) {
		Long id = null;

		if (order.getId() != null){
			log.debug("Updating existing order with ID: {}", order.getId());
			updateOrderStatusFunction(order);
			id = order.getId();
		}

		log.debug("Resulting ID after saveOrUpdate: {}", id);
		return id;
	}

	private void updateOrderStatusFunction(Order order) {
		if (order == null || order.getOrderStatus() == null || order.getOrderStatus().getId() == null) {
			throw new IllegalArgumentException("Order or OrderStatus is invalid");
		}
		String updateSql = "UPDATE crm_order SET order_status_id = ? WHERE id = ?";

		int rowsUpdated = jdbcTemplate0.update(updateSql, order.getOrderStatus().getId(), order.getId());

		if (rowsUpdated > 0) {
			log.debug("Order status updated successfully for order ID: {}", order.getId());
		} else {
			log.warn("No order found with ID: {}", order.getId());
		}
	}

}

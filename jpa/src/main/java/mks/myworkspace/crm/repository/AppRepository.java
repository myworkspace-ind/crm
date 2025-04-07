package mks.myworkspace.crm.repository;

import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerCare;
import mks.myworkspace.crm.entity.EmailToCustomer;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.entity.HistoryOrder;
import mks.myworkspace.crm.entity.Interaction;
import mks.myworkspace.crm.entity.Order;
import mks.myworkspace.crm.entity.OrderCategory;
import mks.myworkspace.crm.entity.OrderStatus;
import mks.myworkspace.crm.entity.Profession;
import mks.myworkspace.crm.entity.ResponsiblePerson;
import mks.myworkspace.crm.entity.Status;

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

	public List<Long> saveOrUpdateInteraction(List<Interaction> entities) {
		// Danh sách để lưu trữ các ID của bản ghi sau khi lưu hoặc cập nhật
		List<Long> ids = new ArrayList<>();

		// Sử dụng SimpleJdbcInsert để thực hiện lưu bản ghi mới
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0)
				.withTableName("crm_customer_interaction")
				.usingColumns("interaction_date", "content", "next_plan", "customer_id", "contact_person", "created_at")
				.usingGeneratedKeyColumns("id");

		for (Interaction entity : entities) {
			Long id;

			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("interaction_date", entity.getInteractionDate())
					.addValue("content", entity.getContent())
					.addValue("next_plan", entity.getNextPlan())
					.addValue("customer_id", entity.getCustomer() != null ? entity.getCustomer().getId() : null)
					.addValue("contact_person", entity.getContactPerson())
					.addValue("created_at", entity.getCreatedAt());

			if (entity.getId() == null) {
				// Nếu ID của thực thể là null, thực hiện lưu mới
				id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
			} else {

				updateEntity(entity);
				id = entity.getId(); // Lấy ID của bản ghi đã cập nhật
			}

			// Thêm ID vào danh sách kết quả
			ids.add(id);
		}

		return ids; // Trả về danh sách ID sau khi xử lý
	}

	public void deleteInteractionById(Long interactionId) {
		// Tạo câu lệnh DELETE với ID duy nhất
		String sql = "DELETE FROM crm_customer_interaction WHERE id = ?";

		// Thực thi câu lệnh DELETE
		int rowsDeleted = jdbcTemplate0.update(sql, interactionId);

		// Log kết quả
		if (rowsDeleted > 0) {
			log.debug("Interaction with ID {} deleted successfully.", interactionId);
		} else {
			log.warn("No interaction found with ID {} to delete.", interactionId);
		}
	}
	public void deletePersonById(Long Id) {
		// Tạo câu lệnh DELETE với ID duy nhất
		String sql = "DELETE FROM crm_responsible_person WHERE id = ?";

		// Thực thi câu lệnh DELETE
		int rowsDeleted = jdbcTemplate0.update(sql, Id);

		// Log kết quả
		if (rowsDeleted > 0) {
			log.debug("Responsible Person with ID {} deleted successfully.", Id);
		} else {
			log.warn("No Responsible Person found with ID {} to delete.", Id);
		}
	}
	public void deleteStatusById(Long Id) {
		// Tạo câu lệnh DELETE với ID duy nhất
		String sql = "DELETE FROM crm_status WHERE id = ?";

		// Thực thi câu lệnh DELETE
		int rowsDeleted = jdbcTemplate0.update(sql, Id);

		// Log kết quả
		if (rowsDeleted > 0) {
			log.debug("Status with ID {} deleted successfully.", Id);
		} else {
			log.warn("No Status found with ID {} to delete.", Id);
		}
	}
	public void deleteProfessionById(Long Id) {
		// Tạo câu lệnh DELETE với ID duy nhất
		String sql = "DELETE FROM crm_profession WHERE id = ?";

		// Thực thi câu lệnh DELETE
		int rowsDeleted = jdbcTemplate0.update(sql, Id);

		// Log kết quả
		if (rowsDeleted > 0) {
			log.debug("Profession with ID {} deleted successfully.", Id);
		} else {
			log.warn("No profession found with ID {} to delete.", Id);
		}
	}

	public void deleteGoodsCategoryById(Long Id) {
		// Tạo câu lệnh DELETE với ID duy nhất
		String sql = "DELETE FROM crm_goodscategory WHERE id = ?";

		// Thực thi câu lệnh DELETE
		int rowsDeleted = jdbcTemplate0.update(sql, Id);

		// Log kết quả
		if (rowsDeleted > 0) {
			log.debug("GoodsCategory with ID {} deleted successfully.", Id);
		} else {
			log.warn("No GoodsCategory found with ID {} to delete.", Id);
		}
	}

	private void updateEntity(Interaction entity) {
		String updateSql = "UPDATE crm_customer_interaction SET " + "interaction_date = ?, " + "content = ?, "
				+ "next_plan = ?, " + "contact_person = ? " + "WHERE id = ?";

		// Execute the update using the JdbcTemplate
		int rowsUpdated = jdbcTemplate0.update(updateSql, entity.getInteractionDate(), entity.getContent(),
				entity.getNextPlan(), entity.getContactPerson(), entity.getId());

		// Log the result
		if (rowsUpdated > 0) {
			log.info("✅ Interaction cập nhật thành công: ID={} | Date={} | Content={} | NextPlan={} | ContactPerson={}",
	                 entity.getId(), entity.getInteractionDate(), entity.getContent(), entity.getNextPlan(), entity.getContactPerson());
		} else {
			log.warn("No interaction found with ID: {}", entity.getId());
		}
	}

	public List<Long> saveOrUpdateOrderCategory(List<OrderCategory> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_ordercategory")
				.usingGeneratedKeyColumns("id");

		Long id;
		for (OrderCategory e : entities) {
			if (e.getId() == null) {
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
			} else {
				// Update
				// update(e);
				id = e.getId();
				updateOrderCategory(e);
			}

			ids.add(id);
		}

		return ids;
	}

	private void updateOrderCategory(OrderCategory e) {
		// TODO Auto-generated method stub
		String updateSql = "UPDATE crm_ordercategory SET name = ?, note = ? WHERE id = ?";

		jdbcTemplate0.update(updateSql, e.getName(), e.getNote(), e.getId());
	}
	public List<Long> saveOrUpdateProfession(List<Profession> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_profession")
				.usingGeneratedKeyColumns("id");

		Long id;
		for (Profession e : entities) {
			if (e.getId() == null) {
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
				updateSeqno(id, "crm_profession");
			} else {
				// Update
				// update(e);
				id = e.getId();
				updateProfession(e);
			}

			ids.add(id);
		}

		return ids;
	}

	private void updateSeqno(Long id, String tableName) {
		String sql = String.format("UPDATE %s SET seqno = ? WHERE id = ?", tableName);
		jdbcTemplate0.update(sql, id, id); // Set seqno = id
	}
	private void updateProfession(Profession e) {
		// TODO Auto-generated method stub
		String updateSql = "UPDATE crm_profession SET name = ?, note = ?, seqno = ? WHERE id = ?";

		jdbcTemplate0.update(updateSql, e.getName(), e.getNote(), e.getSeqno(), e.getId());
	}

	public List<Long> saveOrUpdateGoodsCategory(List<GoodsCategory> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_goodscategory")
				.usingGeneratedKeyColumns("id");

		Long id;
		for (GoodsCategory e : entities) {
			if (e.getId() == null) {
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
				updateSeqno(id, "crm_goodscategory");
			} else {
				// Update
				// update(e);
				id = e.getId();
				updateGoodsCategory(e);
			}

			ids.add(id);
		}

		return ids;
	}
	
	private void updateGoodsCategory(GoodsCategory e) {
		// TODO Auto-generated method stub
		String updateSql = "UPDATE crm_goodscategory SET name = ?, note = ?, seqno = ? WHERE id = ?";

		jdbcTemplate0.update(updateSql, e.getName(), e.getNote(), e.getSeqno(), e.getId());
	}

	public List<Long> saveOrUpdateStatus(List<Status> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_status")
				.usingGeneratedKeyColumns("id");

		Long id;
		for (Status e : entities) {
			if (e.getId() == null) {
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
				updateSeqno(id, "crm_status");
			} else {
				// Update
				// update(e);
				id = e.getId();
				updateStatus(e);
			}

			ids.add(id);
		}

		return ids;
	}

	private void updateStatus(Status e) {
		// TODO Auto-generated method stub
		String updateSql = "UPDATE crm_status SET name = ?, backgroundColor = ?, seqno = ?  WHERE id = ?";

		jdbcTemplate0.update(updateSql, e.getName(), e.getBackgroundColor(), e.getSeqno(), e.getId());
	}

	public List<Long> saveOrUpdateResponsiblePerson(List<ResponsiblePerson> entities) {
		List<Long> ids = new ArrayList<Long>(); // Id of records after save or update.

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_responsible_person")
				.usingGeneratedKeyColumns("id");

		Long id;
		for (ResponsiblePerson e : entities) {
			if (e.getId() == null) {
				id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(e)).longValue();
				updateSeqno(id, "crm_responsible_person");
			} else {
				// Update
				// update(e);
				id = e.getId();
				updateResponsiblePerson(e);
			}

			ids.add(id);
		}

		return ids;
	}

	private void updateResponsiblePerson(ResponsiblePerson e) {
		// TODO Auto-generated method stub
		String updateSql = "UPDATE crm_responsible_person SET name = ?, note = ?, seqno=? WHERE id = ?";

		jdbcTemplate0.update(updateSql, e.getName(), e.getNote(), e.getSeqno(), e.getId());
	}
	public Long saveOrUpdate(Customer customer) {
		Long id;
		if (customer.getAccountStatus() == null) {
			customer.setAccountStatus(true);
		}
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
	
	public Long saveEmailToCustomer(EmailToCustomer emailToCustomer) {
		Long id;
		id = createEmail(emailToCustomer);
		return id;
	}

	private int updateCustomer(Customer customer) {
		// Tạo bản đồ chứa các giá trị cần cập nhật
		Map<String, Object> parameters = new HashMap<>();

		// Thêm các trường cố định trong entity (không có liên kết bảng)
		parameters.put("address", customer.getAddress());
		parameters.put("company_name", customer.getCompanyName());
		parameters.put("contact_person", customer.getContactPerson());
		// parameters.put("updated_at", customer.getUpdatedAt()); // Giả sử bạn có
		// trường `updated_at`
		parameters.put("email", customer.getEmail());
		parameters.put("note", customer.getNote());
		parameters.put("phone", customer.getPhone());

		// Thêm các khóa ngoại
		parameters.put("main_status_id", customer.getMainStatus() != null ? customer.getMainStatus().getId() : null);
		parameters.put("sub_status_id", customer.getSubStatus() != null ? customer.getSubStatus().getId() : null);
		parameters.put("profession_id", customer.getProfession() != null ? customer.getProfession().getId() : null);
		parameters.put("responsible_person_id",
				customer.getResponsiblePerson() != null ? customer.getResponsiblePerson().getId() : null);

		// Chỉ định điều kiện cập nhật (thường là theo ID)
		String sql = "UPDATE crm_customer SET " + "address = :address, " + "company_name = :company_name, "
				+ "contact_person = :contact_person, " +
				// "updated_at = :updated_at, " +
				"email = :email, " + "note = :note, " + "phone = :phone, " + "main_status_id = :main_status_id, "
				+ "sub_status_id = :sub_status_id, " + "profession_id = :profession_id, "
				+ "responsible_person_id = :responsible_person_id " + "WHERE id = :id";

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
		parameters.put("account_status", customer.getAccountStatus());

		// Thêm các khóa ngoại
		parameters.put("main_status_id", customer.getMainStatus() != null ? customer.getMainStatus().getId() : null);
		parameters.put("sub_status_id", customer.getSubStatus() != null ? customer.getSubStatus().getId() : null);
		parameters.put("profession_id", customer.getProfession() != null ? customer.getProfession().getId() : null);
		parameters.put("responsible_person_id",
				customer.getResponsiblePerson() != null ? customer.getResponsiblePerson().getId() : null);

		id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
		log.debug("New ID: {}", id);
		return id;

	}
	
	private Long createEmail(EmailToCustomer emailToCustomer) {
		Long id;
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_emailtocustomer")
				.usingGeneratedKeyColumns("id");
		Map<String, Object> parameters = new HashMap<>();
		
		// Thêm các trường cố định trong entity (không có liên kết bảng)
		String subjectUtf8 = new String(emailToCustomer.getSubject().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		String contentUtf8 = new String(emailToCustomer.getContent().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

		parameters.put("subject", subjectUtf8);
		parameters.put("content", contentUtf8);
		parameters.put("sender", emailToCustomer.getSender());
		
		// Thêm send_date và status
	    parameters.put("send_date", emailToCustomer.getSendDate() != null ? emailToCustomer.getSendDate() : new Date());
	    parameters.put("status", emailToCustomer.getStatus() != null ? emailToCustomer.getStatus().name() : "DRAFT");

		// Thêm các khóa ngoại
		//parameters.put("status", emailToCustomer.getStatus() != null ? emailToCustomer.getStatus().name() : "DRAFT");
		parameters.put("receiver_id", emailToCustomer.getCustomer() != null ? emailToCustomer.getCustomer().getId() : null);
		id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
		
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

	public void hideCustomersByIds(List<Long> customerIds) {
		String sql = "UPDATE crm_customer SET account_status = 0 WHERE id IN ("
				+ customerIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";

		jdbcTemplate0.update(sql, customerIds.toArray());
	}

	public void showHidedCustomers() {
		String sql = "UPDATE crm_customer SET account_status = 1 WHERE account_status = 0";
		jdbcTemplate0.update(sql);
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

		if (order.getId() != null) {
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

	public Long updateCustomerStatus(Customer customer) {
		Long id = null;

		if (customer.getId() != null) {
			log.debug("Updating existing customer with ID: {}", customer.getId());
			updateCustomerStatusFunction(customer);
			id = customer.getId();
		}

		log.debug("Resulting ID after saveOrUpdate: {}", id);
		return id;
	}

	private void updateCustomerStatusFunction(Customer customer) {
		if (customer == null || customer.getMainStatus() == null || customer.getMainStatus().getId() == null
				|| customer.getSubStatus() == null || customer.getSubStatus().getId() == null) {
			throw new IllegalArgumentException("Customer or Statuses are invalid");
		}

		String updateSql = "UPDATE crm_customer SET main_status_id = ?, sub_status_id = ? WHERE id = ?";

		int rowsUpdated = jdbcTemplate0.update(updateSql, customer.getMainStatus().getId(),
				customer.getSubStatus().getId(), customer.getId());

		if (rowsUpdated > 0) {
			log.debug("Customer statuses updated successfully for customer ID: {}", customer.getId());
		} else {
			log.warn("No customer found with ID: {}", customer.getId());
		}
	}

	public void saveHistory(HistoryOrder historyOrder) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate0).withTableName("crm_history_order")
				.usingGeneratedKeyColumns("id");

		Map<String, Object> parameters = new HashMap<>();
		// Thêm khóa ngoại tới đơn hàng
		parameters.put("order_id", historyOrder.getOrder().getId());
		// Thêm khóa ngoại tới trạng thái đơn hàng
		parameters.put("order_status_id", historyOrder.getOrderStatus().getId());
		// Thêm thời gian cập nhật
		parameters.put("updated_at", historyOrder.getUpdatedAt());

		// Thực hiện chèn mà không cần lấy id
		simpleJdbcInsert.execute(parameters);
		log.debug("History record saved successfully.");
	}

	public void swapRowOnHandsontable(Long rowId1, Long rowId2, String table) {
		String sql = String.format("UPDATE %s t1 JOIN %s t2 ON t1.id != t2.id SET t1.seqno = t2.seqno, t2.seqno = t1.seqno WHERE t1.id = ? AND t2.id = ?", table, table);

		jdbcTemplate0.update(sql, rowId1, rowId2);
	}


	public void createOrderStatus(String name) {
		if(name == null || name == "") {
			return;
		}
		else {
			String sql = "Insert into crm_orderstatus (name) values (?)";
			jdbcTemplate0.update(sql,name);
			/* System.out.println("Them vao orderStatus thanh cong"); */
		}
	}
	
	public void deleteOrderCategoryStatus(Long idLoaiDonHang, Long idTrangThai) {
		if(idLoaiDonHang == null || idTrangThai == null) {
			return;
		}
		else {
			String sql = "Delete from order_category_status where order_category_id=? and order_status_id=?";
			jdbcTemplate0.update(sql,idLoaiDonHang,idTrangThai);
			/* System.out.println("Xoa khoi bang loai don hang,trang thai thanh cong"); */
		}
	}
	
	public void insertOrderCategoryStatus(Long idLoaiDonHang, Long idTrangThai) {
		if(idLoaiDonHang == null || idTrangThai == null) {
			return;
		}
		else {
			String sql = "Insert into order_category_status (order_category_id,order_status_id) values (?,?)";
			jdbcTemplate0.update(sql,idLoaiDonHang,idTrangThai);
			/* System.out.println("Xoa khoi bang loai don hang,trang thai thanh cong"); */
		}
	}
	
	public void updateOrderCategory(Long idLoaiDonHang, String name) {
		if(idLoaiDonHang == null || name == null || name.isEmpty()) {
			return;
		}
		else {
			String sql = "update crm_ordercategory set name=? where id=?";
			jdbcTemplate0.update(sql,name,idLoaiDonHang);
			/* System.out.println("Cap Nhat loai don hang thanh cong"); */
		}
	}
	
	//Khong khai bao bien trong vong lap
	public void insertCustomerCare(List<CustomerCare> customerCares, int reminderDays) {
		String checkSql = "SELECT EXISTS(SELECT 1 FROM crm_customer_care WHERE customer_id = ?)";
		String insertSql = "INSERT INTO crm_customer_care (customer_id, remind_date) VALUES (?, ?)";

		Boolean exists;
		LocalDateTime reminderTime;

		for (CustomerCare care : customerCares) {
			exists = jdbcTemplate0.queryForObject(checkSql, Boolean.class, care.getCustomer().getId());

			if (Boolean.FALSE.equals(exists)) { // Nếu chưa tồn tại, thì insert
				if (care.getCustomer().getCreatedAt() != null) {
					reminderTime = care.getCustomer().getCreatedAt().plusDays(reminderDays);
				} else {
					reminderTime = null; 
				}

				jdbcTemplate0.update(insertSql, care.getCustomer().getId(), reminderTime);
			}
		}
	}
	
//	public void updatePriorityCustomerCare(List<CustomerCare> customerCareList) {
//	    String updateSql = "UPDATE crm_customer_care SET priority = ? WHERE id = ?";
//	    
//	    int[] rowsUpdated = jdbcTemplate0.batchUpdate(updateSql, new BatchPreparedStatementSetter() {
//			
//			@Override
//			public void setValues(PreparedStatement ps, int i) throws SQLException {
//				CustomerCare customerCare = customerCareList.get(i);
//				ps.setString(1, customerCare.getPriority());
//				ps.setLong(2, customerCare.getId());
//			}
//			
//			@Override
//			public int getBatchSize() {
//				return customerCareList.size();
//			}
//		});
//	    
//	    int totalUpdated = Arrays.stream(rowsUpdated).sum();
//	    
//	    log.debug("Total {} records updated successfully.", totalUpdated);
//	}
	
	public void updatePriorityCustomerCare(CustomerCare customerCare) {
	    String updateSql = "UPDATE crm_customer_care SET priority = ? WHERE id = ?";
	    int rowsUpdated = jdbcTemplate0.update(updateSql, customerCare.getPriority(), customerCare.getId());
	    	    
	    log.debug("✅ Cập nhật priority thành công cho ID: {}, rowsUpdated={}", customerCare.getId(), rowsUpdated);
	}
	
	public int updateCustomerCareStatus(int reminderDays) {
		String updateSql = 
		        "UPDATE crm_customer_care c " +
		        "SET care_status = " +
		        "    CASE " +
		        "        WHEN EXISTS ( " +
		        "            SELECT 1 FROM crm_customer_interaction i " +
		        "            WHERE i.customer_id = c.customer_id " +
		        "            AND i.created_at BETWEEN c.remind_date AND c.remind_date + INTERVAL ? DAY " +
		        "        ) THEN 'Đã chăm sóc, Chăm sóc đúng hạn' " +

		        "        WHEN EXISTS ( " +
		        "            SELECT 1 FROM crm_customer_interaction i " +
		        "            WHERE i.customer_id = c.customer_id " +
		        "            AND i.created_at > c.remind_date + INTERVAL ? DAY " +
		        "        ) THEN 'Đã chăm sóc, Chăm sóc trễ hạn' " +

		        "        WHEN c.remind_date + INTERVAL ? DAY >= NOW() " +
		        "        THEN 'Chưa chăm sóc' " +

		        "        ELSE 'Chưa chăm sóc, Chăm sóc trễ hạn' " +
		        "    END " +
		        "WHERE c.remind_date IS NOT NULL;";

		    return jdbcTemplate0.update(updateSql, reminderDays, reminderDays, reminderDays);
	}
	
}

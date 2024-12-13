package mks.myworkspace.crm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "crm_history_order")
public class HistoryOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID của lịch sử

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Liên kết với đơn hàng

    @ManyToOne(fetch = FetchType.EAGER) // Fetch type có thể thay đổi tùy theo yêu cầu
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus orderStatus; // Liên kết với trạng thái đơn hàng

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt; // Thời gian cập nhật trạng thái

    public HistoryOrder() {}

    public HistoryOrder(Order order, OrderStatus orderStatus, Date updatedAt) {
        this.order = order;
        this.orderStatus = orderStatus;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

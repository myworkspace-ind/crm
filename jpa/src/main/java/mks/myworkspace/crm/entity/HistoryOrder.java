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
    @JoinColumn(name = "order_id")
    private Order order; // Liên kết với đơn hàng

    @Column(name = "order_status")
    private String orderStatus; // Trạng thái của đơn hàng tại thời điểm cập nhật

    @Column(name = "updated_at")
    private Date updatedAt; // Thời gian cập nhật trạng thái

    public HistoryOrder() {}

    public HistoryOrder(Order order, String orderStatus, Date updatedAt) {
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

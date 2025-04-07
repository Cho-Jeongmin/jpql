package jpql;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class Order {

  @Id
  @GeneratedValue
  private Long id;
  private int orderAmount;
  @ManyToOne
  @JoinColumn(name = "PRODUCT_ID")
  private Product product;
  @Embedded
  private Address address;

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getOrderAmount() {
    return orderAmount;
  }

  public void setOrderAmount(int orderAmount) {
    this.orderAmount = orderAmount;
  }
}

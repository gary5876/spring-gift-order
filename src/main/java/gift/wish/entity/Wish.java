package gift.wish.entity;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.option.entity.Option;
import jakarta.persistence.*;

@Entity
@Table(name = "wishes")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_id")
    private Option option;

    @Column(nullable = false)
    private int quantity;

    protected Wish() {
    }

    public Wish(Member member, Product product, Option option) {
        this.member = member;
        this.product = product;
        this.option = option;
        this.quantity = 1;
    }

    public Wish(Member member, Product product, Option option,int quantity) {
        this.member = member;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
    }

    public Wish(Member member, Product product, String option) {
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }
    
    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}

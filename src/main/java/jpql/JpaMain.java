package jpql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class JpaMain {

  public static void main(String[] args) {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    EntityManager em = emf.createEntityManager();

    EntityTransaction tx = em.getTransaction();
    tx.begin();

    try {

      // 데이터 100개 삽입
      for (int i = 0; i < 100; i++) {
        Member member = new Member();
        member.setUsername("member" + i);
        member.setAge(i);
        em.persist(member);
      }

      // 페이징 (setFirstResult, setMaxResult)
      List<Member> result = em.createQuery("select m from Member m order by m.age desc",
          Member.class).setFirstResult(20).setMaxResults(10).getResultList();

      System.out.println("result.size() = " + result.size());
      for (Member member1 : result) {
        System.out.println("member1 = " + member1);
      }

      tx.commit();
    } catch (Exception e) {
      e.printStackTrace();
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }
}
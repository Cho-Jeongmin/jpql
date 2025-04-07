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
      Member member = new Member();
      member.setUsername("member1");
      member.setAge(10);
      em.persist(member);

      // 반환 타입이 명확하면 TypedQuery로 반환됨
      TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
      TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);

      // 반환 타입이 불명확하면 Query로 반환됨
      Query query3 = em.createQuery("select m.username, m.age from Member m");

      // 결과가 여러 개면 getResultList (결과가 업으면 빈 리스트 반환)
      List<Member> resultList = query1.getResultList();
      for (Member member1 : resultList) {
        System.out.println("member1 = " + member1);
      }

      // 결과 값이 딱 하나면 getSingleResult
      // (결과가 없거나 둘 이상이면 예외 발생. Spring Data JPA의 경우 결과가 없더라도 Optional로 반환해서 괜찮음)
      Member result = query1.getSingleResult();
      System.out.println("result = " + result);

      // 파라미터 바인딩
      Member singleResult = em.createQuery(
              "select m from Member m where m.username = :username", Member.class)
          .setParameter("username", "member1").getSingleResult();
      for (Member member1 : resultList) {
        System.out.println("singleResult = " + singleResult.getUsername());
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
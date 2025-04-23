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

      //1️⃣ 엔티티 프로젝션 (select 절에서 엔티티 m을 조회해온다는 뜻)
      List<Member> resultList = em.createQuery("select m from Member m", Member.class)
          .getResultList();

      Member findMember = resultList.get(0);
      findMember.setAge(20); // findMember는 영속성 컨텍스트에서 관리되므로 여기서 update 쿼리가 나감

      List<Team> resultList1 = em.createQuery("select m.team from Member m", Team.class)
          .getResultList(); // 실제 쿼리에선 MEMBER와 TEAM을 조인하게 되는데, 여기서 그게 명확히 보이지 않음.(묵시적 조인)

      List<Team> resultList2 = em.createQuery("select m.team from Member m join m.team t",
              Team.class)
          .getResultList(); // 이런식으로 명확하게 조인을 써줘야 실제 SQL을 예상할 수 있으므로 좋음.(명시적 조인)

      //2️⃣ 임베디드 타입 프로젝션
      List<Address> resultList3 = em.createQuery("select o.address from Order o",
          Address.class).getResultList();

      //3️⃣ 스칼라 타입 프로젝션
      // 3.1 Query 타입으로 조회(타입 불명확)
      List resultList4 = em.createQuery("select distinct m.username, m.age from Member m")
          .getResultList();

      Object result1 = resultList4.get(0); // 반환타입이 명확하지 않으므로 Object로 캐스팅 해줘야 함.
      Object[] r1 = (Object[]) result1;
      System.out.println("username = " + r1[0]);
      System.out.println("age = " + r1[1]);

      // 3.2 Object[] 타입으로 조회 - 제네릭 타입을 명시해주면 캐스팅 과정 생략 가능
      List<Object[]> resultList5 = em.createQuery("select distinct m.username, m.age from Member m")
          .getResultList();

      Object[] r2 = resultList5.get(0);
      System.out.println("username = " + r2[0]);
      System.out.println("age = " + r2[1]);

      // 3.3 new 명령어로 조회 - 제일 좋은 방법. DTO를 만들어서 맵핑
      List<MemberDTO> resultList6 = em.createQuery(
              "select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
          .getResultList(); // 마치 DTO 클래스의 생성자를 호출하듯이 작성 (패키지명을 포함한 전체 클래스명 작성)

      MemberDTO r3 = resultList6.get(0);
      System.out.println("username = " + r3.getUsername());
      System.out.println("age = " + r3.getAge());

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
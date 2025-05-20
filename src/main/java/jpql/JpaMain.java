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

      Team team = new Team();
      team.setName("teamA");
      em.persist(team);

      Member member = new Member();
      member.setUsername("member1");
      member.setAge(10);
      member.changeTeam(team);
      em.persist(member);

      // INNER JOIN (팀에 속한 멤버만 조회됨)
      String query1 = "select m from Member m join m.team t where t.name = :teamName";
      List<Member> result1 = em.createQuery(query1, Member.class).setParameter("teamName", "teamA")
          .getResultList();

      // LEFT OUTER JOIN (팀에 속하지 않은 멤버도 조회됨)
      String query2 = "select m from Member m left join m.team t";
      List<Member> result2 = em.createQuery(query2, Member.class).getResultList();

      // THETA JOIN (멤버와 팀이 곱하기로 다 조회됨 - 카르테시안 곱)
      String query3 = "select m from Member m, Team t where m.username = t.name";
      List<Member> result3 = em.createQuery(query3, Member.class).getResultList();

      // 조인 대상 필터링 (회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인)
      String query4 = "select m, t from Member m left join m.team t on t.name = 'A'";

      // 연관관계 없는 엔티티 외부 조인
      String query5 = "select m, t from Member m left join Team t on m.username = t.name";

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
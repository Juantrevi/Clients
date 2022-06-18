//package com.example.datajp.Dao;
//
//import com.example.datajp.Entities.Cliente;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.List;
//
//@Repository
//public class ClienteDaoImpl implements IClienteDao{
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @SuppressWarnings("unchecked")
//    @Transactional(readOnly = true)
//    @Override
//    public List findAll() {
//        return em.createQuery("from Cliente").getResultList();
//    }
//
//    @Transactional
//    @Override
//    public void save(Cliente cliente) {
//        if (cliente.getId() != null && cliente.getId() > 0){
//            em.merge(cliente);
//        }else {
//            em.persist(cliente);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Cliente findOne(Long id) {
//        return em.find(Cliente.class, id);
//    }
//
//    @Override
//    @Transactional
//    public void delete(Long id) {
//        em.remove(findOne(id));
//    }
//}

package CanalDev.Financia.Repositorio;

import CanalDev.Financia.Modelo.MovimientoModelo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepositorio extends JpaRepository<MovimientoModelo, Integer> {
}

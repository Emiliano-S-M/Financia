package CanalDev.Financia.Servicio;

import CanalDev.Financia.Modelo.MovimientoModelo;
import CanalDev.Financia.Repositorio.MovimientoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static CanalDev.Financia.Utils.MensajeUtil.mostrarError;

@Service
public class MovimientoServicio implements IMovimientoServicio{

    @Autowired
    private MovimientoRepositorio movimientoRepositorio;

    @Override
    public List<MovimientoModelo> listarMovimientos() {
        return movimientoRepositorio.findAll();
    }

    @Override
    public MovimientoModelo guardarMovimiento(MovimientoModelo movimiento) {
        return movimientoRepositorio.save(movimiento);
    }

    @Override
    public boolean eliminarMovimiento(MovimientoModelo movimiento) {
        try {
            movimientoRepositorio.delete(movimiento);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean eliminarMovimientoPorId(Integer id){
        try {
            movimientoRepositorio.deleteById(id);
            return true;
        } catch (Exception e) {
            mostrarError("Error", "No se pudo eliminar movimiento en la base de datos");
            return false;
        }

    }
}

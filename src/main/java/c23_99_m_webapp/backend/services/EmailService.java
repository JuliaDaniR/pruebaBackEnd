package c23_99_m_webapp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void getEmailAdmin(String email, String fullName, String password, String institutionName) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("\uD83C\uDFEB ClassKit\n\n" + "Registro de Administrador - Escuela: " + institutionName);
        mailMessage.setText(String.format(
                "Hola %s,\n\nTe has registrado exitosamente como Administrador para la institución %s.\n\n" +
                        "Tus credenciales son:\nUsuario: %s\nContraseña: %s\n\nSaludos,\nEl Equipo.",
                fullName, institutionName, email, password));
        javaMailSender.send(mailMessage);
    }

    public void getEmailTeacher(String email, String fullName, String password, String institutionName) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("\uD83C\uDFEB ClassKit\n\n" + "Registro de Docente - Escuela: " + institutionName);
        mailMessage.setText(String.format(
                "Hola %s,\n\nTe has registrado exitosamente como Docente para la institución %s.\n\n" +
                        "Tus credenciales son:\nUsuario: %s\nContraseña: %s\n\nPor favor, cambia tu contraseña después de iniciar sesión.\n\nSaludos,\nEl Equipo.",
                fullName, institutionName, email, password));
        javaMailSender.send(mailMessage);
    }

    public void getEmailTeacherUpdate(String email, String fullName, String password, String institutionName) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("\uD83C\uDFEB ClassKit\n\n" + "Modificación de datos del Docente - ClassKit - Escuela: " + institutionName);
        mailMessage.setText(String.format(
                "Hola %s,\n\nTe datos se han actualizado exitosamente %s.\n\n" +
                        "Tus nuevas credenciales son:\nUsuario: %s\nContraseña: %s\n\nSaludos,\nEl Equipo.",
                fullName, institutionName, email, password));
        javaMailSender.send(mailMessage);
    }
}

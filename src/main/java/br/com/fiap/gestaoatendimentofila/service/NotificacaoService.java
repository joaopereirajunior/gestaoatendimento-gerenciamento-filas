package br.com.fiap.gestaoatendimentofila.service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class NotificacaoService {
    @Value("${twilio.phone.from}")
    private String fromNumber;

    public void enviarSms(String numeroPaciente, String mensagem) {
        Message.creator(
                new PhoneNumber(numeroPaciente),
                new PhoneNumber(fromNumber),
                mensagem
        ).create();
    }


}

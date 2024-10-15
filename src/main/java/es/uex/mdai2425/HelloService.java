package es.uex.mdai2425;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public String getHello()
    {
        return "Hola Mundo";
    }
}

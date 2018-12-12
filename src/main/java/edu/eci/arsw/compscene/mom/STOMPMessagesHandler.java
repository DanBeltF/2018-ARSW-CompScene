/*
 * The MIT License
 *
 * Copyright 2018 Pivotal Software, Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.eci.arsw.compscene.mom;

import edu.eci.arsw.compscene.model.Jugador;
import edu.eci.arsw.compscene.model.Pregunta;
import edu.eci.arsw.compscene.persistence.impl.Tupla;
import edu.eci.arsw.compscene.services.CompSceneServices;
import edu.eci.arsw.compscene.services.CompSceneServicesException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

/**
 *
 * @author dbeltran
 */
@Controller
@EnableScheduling
public class STOMPMessagesHandler {
        private static Logger logger=Logger.getLogger(STOMPMessagesHandler.class);
        private List<Jugador> participantes=new ArrayList<Jugador>();
    
        
        private String nomre;
        
	@Autowired
	SimpMessagingTemplate msgt;
      
        @Autowired
        private CompSceneServices compserv;
        
	@MessageMapping("/weather")    
        @SendTo("/topic/weatherinfo")
	public String recibirJugador(JugadorInfo jugador) throws Exception {
                String wInfo = null;
                logger.info("Llegamos");
		try {
                    logger.info("de veras");
			logger.info("Get weather from " + jugador.getJugador());
                        compserv.addJugador(jugador.getJugador());
                        nomre=jugador.getJugador();
                        //int temp=compserv.getIdJugador(jugador.getJugador());
                        Jugador tempp=compserv.getJUgador(jugador.getJugador());
                        
			/* Lets make a pause */
			Thread.sleep(1000);

			/* Return weather info for the given place */
			wInfo = tempp.toString();
                        
                           
                        

			logger.info("Retrieving weather from " + jugador.getJugador());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return wInfo;
            //return compserv.getJUgador(temp);
	}
        
        
        public void runWInfo() throws CompSceneServicesException {
		this.msgt.convertAndSend("/topic/weatherinfo",
				compserv.jugadorToString(nomre));
	}
}
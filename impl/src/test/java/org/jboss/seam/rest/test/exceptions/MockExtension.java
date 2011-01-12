/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.rest.test.exceptions;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.seam.rest.SeamRestConfiguration;
import org.jboss.seam.rest.exceptions.SeamExceptionMapper;
import org.jboss.seam.rest.util.ExpressionLanguageInterpolator;
import org.jboss.seam.rest.util.Utils;
import org.jboss.seam.rest.validation.ValidationExceptionHandler;

/**
 * Simulates {@link SeamRestException} either registering {@link SeamExceptionMapper}
 * or {@link CatchExceptionMapper} but not both.
 * @author <a href="mailto:jharting@redhat.com">Jozef Hartinger</a>
 *
 */
public class MockExtension implements Extension
{
   public void registerSeam(@Observes BeforeBeanDiscovery event, BeanManager manager)
   {
      event.addAnnotatedType(manager.createAnnotatedType(ExpressionLanguageInterpolator.class));
      event.addAnnotatedType(manager.createAnnotatedType(ValidationExceptionHandler.class));
   }
   
   public void vetoSeamExceptionMapper(@Observes ProcessAnnotatedType<SeamExceptionMapper> event)
   {
      if (event.getAnnotatedType().getJavaClass().equals(SeamExceptionMapper.class) && Utils.isClassAvailable("org.jboss.seam.exception.control.extension.CatchExtension"))
      {
         event.veto();
      }
   }
   
   public void vetoSeamRestConfiguration(@Observes ProcessAnnotatedType<SeamRestConfiguration> event)
   {
      if (event.getAnnotatedType().getJavaClass().equals(SeamRestConfiguration.class))
      {
         event.veto();
      }
   }
}

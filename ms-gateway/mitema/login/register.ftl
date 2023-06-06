<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        
    <#elseif section = "form">
        <form id="kc-register-form" class="${properties.kcFormClass!}" action="${url.registrationAction}" method="post">
          <div class="navbar-brand">
              <h2 style="color: #c7bad8; margin-bottom: 6px; font-family: 'Fredoka';">storyverse</h2>
              <div class="logo">
                  <h2 style="color: white; ">sv.</h2>
              </div>
          </div>
          <span class="login100-form-title p-b-49">
              Regístrate
          </span>
            <div class="kcFormGroupClass ${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('email',properties.kcFormGroupErrorClass!)}">
                <span class="label-input100">Correo electrónico</span>
                <input type="email" id="email" placeholder="Ingresa tu correo electrónico" class="kcInputClass ${properties.kcInputClass!}" name="email" value="${(register.formData.email!'')}" autocomplete="email" />
                <span class="focus-input100" data-symbol="&#xf15a;"></span>    
            </div>

            <#if !realm.registrationEmailAsUsername>
              <div class="kcFormGroupClass ${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('username',properties.kcFormGroupErrorClass!)}">
                  <span class="label-input100">Nombre de usuario</span>
                  <input placeholder="Ingresa tu nombre de usuario" type="text" id="username" class="kcInputClass ${properties.kcInputClass!}" name="username" value="${(register.formData.username!'')}" autocomplete="username" />
                  <span class="focus-input100" data-symbol="&#xf206;"></span>
              </div>
            </#if>

            <div class="kcFormGroupClass">
               <span class="label-input100">Fecha de nacimiento</span>
               <input style="color:#ADADAD" type="date" class="kcInputClass ${properties.kcInputClass!}" id="user.attributes.birthdate" name="user.attributes.birthdate" value="${(register.formData['user.attributes.birthdate']!'')}"/>
               <span class="focus-input100" data-symbol="&#xf332;"></span>
            </div>


            <div style="display:none;" class="kcFormGroupClass">
               <div class="${properties.kcLabelWrapperClass!}">
                   <label for="user.attributes.library_private" class="${properties.kcLabelClass!}">library_private</label>
               </div>
            
               <div class="${properties.kcInputWrapperClass!}">
                   <input type="text" class="${properties.kcInputClass!}" id="user.attributes.library_private" name="user.attributes.library_private" value="${(register.formData['user.attributes.library_private']!'')}"/>
               </div>
            </div>

            <div style="display:none;" class="kcFormGroupClass">
               <div class="${properties.kcLabelWrapperClass!}">
                   <label for="user.attributes.url_pfp" class="${properties.kcLabelClass!}">url_pfp</label>
               </div>
            
               <div class="${properties.kcInputWrapperClass!}">
                   <input type="text" class="${properties.kcInputClass!}" id="user.attributes.url_pfp" name="user.attributes.url_pfp" value="${(register.formData['user.attributes.url_pfp']!'')}"/>
               </div>
            </div>

            <div style="display:none;" class="kcFormGroupClass">
               <div class="${properties.kcLabelWrapperClass!}">
                   <label for="user.attributes.url_header" class="${properties.kcLabelClass!}">url_header</label>
               </div>
            
               <div class="${properties.kcInputWrapperClass!}">
                   <input type="text" class="${properties.kcInputClass!}" id="user.attributes.url_header" name="user.attributes.url_header" value="${(register.formData['user.attributes.url_header']!'')}"/>
               </div>
            </div>

            <div style="display:none;" class="kcFormGroupClass">
               <div class="${properties.kcLabelWrapperClass!}">
                   <label for="user.attributes.description" class="${properties.kcLabelClass!}">Description</label>
               </div>
            
               <div class="${properties.kcInputWrapperClass!}">
                   <input type="text" class="${properties.kcInputClass!}" id="user.attributes.description" name="user.attributes.description" value="${(register.formData['user.attributes.description']!'')}"/>
               </div>
            </div>
            
            

            <#if passwordRequired>
            <div class="kcFormGroupClass ${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('password',properties.kcFormGroupErrorClass!)}">
                <span class="label-input100">Contraseña</span>
                <input placeholder="Ingresa tu contraseña" type="password" id="password" class="kcInputClass ${properties.kcInputClass!}" name="password" autocomplete="new-password"/>
                <span class="focus-input100" data-symbol="&#xf18f;"></span>
                <i class="zmdi zmdi-eye" id="eye"></i>
            </div>

            <div class="kcFormGroupClass ${properties.kcFormGroupClass!} ${messagesPerField.printIfExists('password-confirm',properties.kcFormGroupErrorClass!)}">
                <span class="label-input100">Confirma tu contraseña</span>
                    <input placeholder="Ingresa nuevamente tu contraseña" type="password" id="password-confirm" class="kcInputClass ${properties.kcInputClass!}" name="password-confirm" />
                <span class="focus-input100" data-symbol="&#xf190;"></span>
                <i id="eye-confirm" class="zmdi zmdi-eye"></i>
            </div>
            </#if>

            <#if recaptchaRequired??>
            <div class="form-group">
                <div class="${properties.kcInputWrapperClass!}">
                    <div class="g-recaptcha" data-size="compact" data-sitekey="${recaptchaSiteKey}"></div>
                </div>
            </div>
            </#if>

            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="return-link ${properties.kcFormOptionsWrapperClass!}">
                        <span><a href="${url.loginUrl}">« Regresar</a></span>
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="login100-form-bgbtn"></div>
                    <input id="kc-register-button" class="kcButtonClass ${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="Registrar"/>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>
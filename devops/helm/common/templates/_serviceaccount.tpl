{{- define "common.serviceaccount" -}}
{{- if .Values.vault.enabled }}
apiVersion: v1
kind: ServiceAccount
metadata:
  name: {{ .Chart.Name }}-sa
  annotations:
    eks.amazonaws.com/role-arn: {{ .Values.vault.irsaRoleArn | default "" | quote }}
{{- end }}
{{- end -}}

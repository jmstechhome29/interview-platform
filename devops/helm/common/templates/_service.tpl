{{- define "common.service" -}}
apiVersion: v1
kind: Service
metadata:
  name: {{ .Chart.Name }}
  labels:
    app: {{ .Chart.Name }}
spec:
  type: ClusterIP
  selector:
    app: {{ .Chart.Name }}
  ports:
    - port: {{ .Values.containerPort }}
      targetPort: {{ .Values.containerPort }}
{{- end -}}
